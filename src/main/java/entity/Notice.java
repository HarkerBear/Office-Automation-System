package entity;

import java.util.Date;

public class Notice {
    private long noticeId;
    private long receiverId;
    private String content;
    private Date createTime;

    public Notice() {
    }

    public Notice(long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
        this.createTime=new Date();
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
