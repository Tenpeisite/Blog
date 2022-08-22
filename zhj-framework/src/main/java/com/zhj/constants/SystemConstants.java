package com.zhj.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL="0";
    /**
     * 友链审核状态通过
     */
    public static final String LINK_status_normal="0";
    /**
     * redis用户id前缀
     */
    public static final String REDIS_LOGINUSER_PREFIX="bloglogin:";
    /**
     * 评论为根评论
     */
    public static final int COMMENT_STATUS_NORMAL = -1;

}