package ru.emplosecrets.web.db;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import ru.emplosecrets.web.beans.Pager;
import ru.emplosecrets.web.beans.CommentBean;
import ru.emplosecrets.web.entity.City;
import ru.emplosecrets.web.entity.Employer;
import ru.emplosecrets.web.entity.Comment;
import ru.emplosecrets.web.entity.Industry;
import ru.emplosecrets.web.entity.HibernateUtil;
import ru.emplosecrets.web.entity.Vote;

public class DataHelper {

    private SessionFactory sessionFactory = null;
    private DetachedCriteria employerListCriteria;
    private DetachedCriteria employersCountCriteria;

    private DetachedCriteria commentListCriteria;
    private DetachedCriteria commentsCountCriteria;

    private ProjectionList employerProjection;
    private ProjectionList commentProjection;
    private Pager pager;
    private CommentBean commentBean;

    public DataHelper(Pager pager) {

        this.pager = pager;

        prepareCriterias();
        prepareCommentCriterias();

        sessionFactory = HibernateUtil.getSessionFactory();

        employerProjection = Projections.projectionList();
        employerProjection.add(Projections.property("emploId"), "emploId");
        employerProjection.add(Projections.property("city"), "city");
        employerProjection.add(Projections.property("industry"), "industry");
        employerProjection.add(Projections.property("name"), "name");
        employerProjection.add(Projections.property("image"), "image");
        employerProjection.add(Projections.property("telephone"), "telephone");
        employerProjection.add(Projections.property("email"), "email");
        employerProjection.add(Projections.property("site"), "site");
        employerProjection.add(Projections.property("descr"), "descr");
        employerProjection.add(Projections.property("rating"), "rating");
        employerProjection.add(Projections.property("voteCount"), "voteCount");

        getAllEmployers();
    }

    public DataHelper(Pager pager, CommentBean commentBean) {

        this.pager = pager;
        this.commentBean = commentBean;

        prepareCriterias();
        prepareCommentCriterias();

        sessionFactory = HibernateUtil.getSessionFactory();

        employerProjection = Projections.projectionList();
        employerProjection.add(Projections.property("emploId"), "emploId");
        employerProjection.add(Projections.property("city"), "city");
        employerProjection.add(Projections.property("industry"), "industry");
        employerProjection.add(Projections.property("name"), "name");
        employerProjection.add(Projections.property("image"), "image");
        employerProjection.add(Projections.property("telephone"), "telephone");
        employerProjection.add(Projections.property("email"), "email");
        employerProjection.add(Projections.property("site"), "site");
        employerProjection.add(Projections.property("descr"), "descr");
        employerProjection.add(Projections.property("rating"), "rating");
        employerProjection.add(Projections.property("voteCount"), "voteCount");

        commentProjection = Projections.projectionList();
        commentProjection.add(Projections.property("comId"), "comId");
        commentProjection.add(Projections.property("employer"), "employer");
        commentProjection.add(Projections.property("user"), "user");
        commentProjection.add(Projections.property("date"), "date");
        commentProjection.add(Projections.property("text"), "text");

        getAllEmployers();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Industry> getAllIndustries() {
        return getSession().createCriteria(Industry.class).list();
    }

    public List<City> getAllCities() {
        return getSession().createCriteria(City.class).list();
    }

    public void getEmployersByRate(String order) {
        prepareOrderedCriterias("rating", order);
        populateList();
    }

    public void getAllEmployers() {
        prepareCriterias();
        populateList();
    }

    public void getEmployersByIndustry(Long indId) {

        Criterion criterion = Restrictions.eq("industry.indId", indId);

        prepareCriterias(criterion);
        populateList();
    }

    public void getEmployersByCity(Long cityId) {

        Criterion criterion = Restrictions.eq("city.cityId", cityId);

        prepareCriterias(criterion);
        populateList();
    }

    public void getCommentsByEmployer(Long emploId) {

        Criterion criterion = Restrictions.eq("employer.emploId", emploId);

        prepareCommentCriterias(criterion);
        populateCommentList();
    }

    public boolean isNameExist(String name, Long id) {

        Criteria criteria = getSession().createCriteria(Employer.class, "em");
        createAliases(criteria);
        criteria.add(Restrictions.ilike("em.name", name, MatchMode.EXACT));

        if (id != null) {
            criteria.add(Restrictions.not(Restrictions.eq("em.emploId", id)));
        }

        Integer total = Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());

        return total >= 1;

    }

    public void getEmployersByName(String employerName) {

        Criterion criterion = Restrictions.ilike("em.name", employerName, MatchMode.ANYWHERE);

        prepareCriterias(criterion);
        populateList();
    }

    private void runEmployerListCriteria() {
        Criteria criteria = employerListCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.asc("em.name")).setProjection(employerProjection).setResultTransformer(Transformers.aliasToBean(Employer.class));

        criteria.setFirstResult(pager.getFrom()).setMaxResults(pager.getTo());

        List<Employer> list = criteria.list();
        pager.setList(list);
    }

    private void runCountCriteria() {
        Criteria criteria = employersCountCriteria.getExecutableCriteria(getSession());
        Integer total = Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
        pager.setTotalEmployersCount(total);
    }

    public void updateEmployer(Employer employer) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update Employer ");
        queryBuilder.append("set name = :name, ");
        queryBuilder.append("city = :city, ");
        queryBuilder.append("industry = :industry, ");
        queryBuilder.append("telephone = :telephone, ");
        queryBuilder.append("email = :email, ");
        queryBuilder.append("site = :site, ");

        if (employer.getUploadedImage() != null) {
            queryBuilder.append("image = :image, ");
        }

        queryBuilder.append("descr = :descr ");

        queryBuilder.append("where emploId = :emploId");

        Query query = getSession().createQuery(queryBuilder.toString());

        query.setParameter("name", employer.getName());
        query.setParameter("telephone", employer.getTelephone());
        query.setParameter("email", employer.getEmail());
        query.setParameter("industry", employer.getIndustry());
        query.setParameter("city", employer.getCity());
        query.setParameter("site", employer.getSite());
        query.setParameter("descr", employer.getDescr());
        query.setParameter("emploId", employer.getEmploId());

        if (employer.getUploadedImage() != null) {
            query.setParameter("image", employer.getUploadedImage());
        }

        int result = query.executeUpdate();

    }

    public void addComment(Comment comment) {
        getSession().save(comment);
    }

    public void addEmployer(Employer employer) {
        getSession().save(employer);
    }

    public Long getUserIdByUserName(String username) {

        Query query = getSession().createQuery("select U.id from User U where U.username = :username");
        query.setParameter("username", username);
        List list = query.list();
        return (Long) list.get(0);
    }

    public void rateEmployer(Employer employer, String username) {
        Vote vote = new Vote();
        vote.setEmployer(employer);
        vote.setUserid(getUserIdByUserName(username));
        vote.setValue(employer.getRating());
        getSession().save(vote);

        updateEmployerRate(employer);

    }

    private void updateEmployerRate(Employer employer) {

        Query query = getSession().createQuery("select new map(round(avg(value)) as rating, count(value) as voteCount)  from Vote v where v.employer.emploId=:emploId");
        query.setParameter("emploId", employer.getEmploId());

        List list = query.list();

        HashMap<String, Object> map = (HashMap<String, Object>) list.get(0);

        long voteCount = Long.valueOf(map.get("voteCount").toString());
        int rating = Double.valueOf(map.get("rating").toString()).intValue();;
        query = getSession().createQuery("update Employer set rating = :rating, "
                + " voteCount = :voteCount"
                + " where emploId = :emploId");

        query.setParameter("rating", rating);
        query.setParameter("voteCount", voteCount);
        query.setParameter("emploId", employer.getEmploId());

        int result = query.executeUpdate();

    }

    public void deleteEmployer(Employer employer) {
        Query query = getSession().createQuery("delete from Employer where emplo_id = :emplo_id");
        query.setParameter("emplo_id", employer.getEmploId());
        int result = query.executeUpdate();
    }

    public void deleteComment(Comment comment) {
        Query query = getSession().createQuery("delete from Comment where comId = :comId");
        query.setParameter("comId", comment.getComId());
        int result = query.executeUpdate();
    }

    private void prepareCriterias(Criterion criterion) {
        employerListCriteria = DetachedCriteria.forClass(Employer.class, "em");
        createAliases(employerListCriteria);
        employerListCriteria.add(criterion);

        employersCountCriteria = DetachedCriteria.forClass(Employer.class, "em");
        createAliases(employersCountCriteria);
        employersCountCriteria.add(criterion);
    }

    private void prepareCriterias() {
        employerListCriteria = DetachedCriteria.forClass(Employer.class, "em");
        createAliases(employerListCriteria);

        employersCountCriteria = DetachedCriteria.forClass(Employer.class, "em");
        createAliases(employersCountCriteria);
    }

    private void prepareOrderedCriterias(String field, String order) {
        employerListCriteria = DetachedCriteria.forClass(Employer.class, "em");

        if (order.equals("desc")) {
            employerListCriteria.addOrder(Order.desc("em." + field));
        } else {
            employerListCriteria.addOrder(Order.asc("em." + field));
        }

        createAliases(employerListCriteria);

        employersCountCriteria = DetachedCriteria.forClass(Employer.class, "em");
        createAliases(employersCountCriteria);
    }

    private void createAliases(DetachedCriteria criteria) {
        criteria.createAlias("em.city", "city");
        criteria.createAlias("em.industry", "industry");

    }

    private void createAliases(Criteria criteria) {
        criteria.createAlias("em.city", "city");
        criteria.createAlias("em.industry", "industry");

    }

    public void populateList() {
        runCountCriteria();
        runEmployerListCriteria();
    }

    private void runCommentListCriteria() {
        Criteria criteria = commentListCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.asc("com.date")).setProjection(commentProjection).setResultTransformer(Transformers.aliasToBean(Comment.class));

        List<Comment> list = criteria.list();
        commentBean.setList(list);
    }

    private void runCommentCountCriteria() {
        Criteria criteria = commentsCountCriteria.getExecutableCriteria(getSession());
        Integer total = Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
        commentBean.setTotalCommentsCount(total);
    }

    public void prepareCommentCriterias(Criterion criterion) {
        commentListCriteria = DetachedCriteria.forClass(Comment.class, "com");
        createCommentAliases(commentListCriteria);
        commentListCriteria.add(criterion);

        commentsCountCriteria = DetachedCriteria.forClass(Comment.class, "com");
        createCommentAliases(commentsCountCriteria);
        commentsCountCriteria.add(criterion);
    }

    private void prepareCommentCriterias() {
        commentListCriteria = DetachedCriteria.forClass(Comment.class, "com");
        createCommentAliases(commentListCriteria);

        commentsCountCriteria = DetachedCriteria.forClass(Comment.class, "com");
        createCommentAliases(commentsCountCriteria);
    }

    private void createCommentAliases(DetachedCriteria criteria) {
        criteria.createAlias("com.employer", "employer");
        criteria.createAlias("com.user", "user");

    }

    public void populateCommentList() {
        runCommentCountCriteria();
        runCommentListCriteria();
    }
}
