package ru.emplosecrets.web.beans;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.List;
import javax.faces.bean.SessionScoped;
import ru.emplosecrets.web.entity.Comment;

@SessionScoped
public class CommentBean {

    private static CommentBean commentBean;

    public CommentBean() {
    }

    private int totalCommentsCount;
    private Comment selectedComment;
    private List<Comment> list;

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public void setTotalCommentsCount(int totalCommentsCount) {
        this.totalCommentsCount = totalCommentsCount;
    }

    public int getTotalCommentsCount() {
        return totalCommentsCount;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

}
