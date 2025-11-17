package top.gaogle.pojo.model.register;

import top.gaogle.pojo.entity.register.Comment;

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
