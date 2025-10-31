package top.gaogle.model.register;

import top.gaogle.domain.register.Comment;

import java.util.List;

public class CommentModel extends Comment {
    private List<CommentModel> children;

    public List<CommentModel> getChildren() {
        return children;
    }

    public void setChildren(List<CommentModel> children) {
        this.children = children;
    }
}
