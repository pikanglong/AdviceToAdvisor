package com.pikanglong.advicetoadvisor.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-28 16:32
 */
public class ProblemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int rank;
    private String content;
    private int type;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private List<OptionEntity> optionEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<OptionEntity> getOptionEntities() {
        return optionEntities;
    }

    public void setOptionEntities(List<OptionEntity> optionEntities) {
        this.optionEntities = optionEntities;
    }

    @Override
    public String toString() {
        return "ProblemEntity{" +
                "id=" + id +
                ", rank=" + rank +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", optionEntities=" + optionEntities +
                '}';
    }
}
