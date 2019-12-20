package com.jzb.base.entity.uploader;

import java.io.Serializable;
import java.util.List;


public class FileInfo implements Serializable {
    private static final long serialVersionUID = 3816110923830675611L;

    private Long id;


    private String filename;


    private String identifier;


    private Long totalSize;


    private Integer type;

    private Long addtime;

    private String uid;

    private String description;

    private String uptime;

    private List<FileInfo> Children;

    private String href;

    private String relativePath;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<FileInfo> getChildren() {
        return Children;
    }

    public void setChildren(List<FileInfo> children) {
        Children = children;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;

}
