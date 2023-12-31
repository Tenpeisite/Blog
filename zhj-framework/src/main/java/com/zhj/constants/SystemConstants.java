package com.zhj.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT = "1";
    /**
     *  文章是正常分布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0";
    /**
     * 文章浏览量前缀
     */
    public static final String REDIS_VIEWCOUNT_PREFIX="article:viewCount";
    /**
     * 文章评论类型
     */
    public static final String ARTICLE_TYPE="0";
    /**
     * 文章置顶
     */
    public static final String ARTICLE_IS_TOP = "1";
    /**
     * 文章非置顶
     */
    public static final String ARTICLE_ISNOT_TOP = "0";

    /**
     * 分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL="0";

    /**
     * 友链审核状态通过
     */
    public static final String LINK_status_normal="0";
    /**
     * 友链评论类型
     */
    public static final String YOULIAN_TYPE="1";

    /**
     * 角色状态正常
     */
    public static final String ROLE_STATUS_NORMAL="0";

    /**
     * redis用户id前缀
     */
    public static final String REDIS_LOGINUSER_PREFIX="bloglogin:";
    public static final String REDIS_BACKSTAGELOGINUSER_PREFIX="login:";
    /**
     * 评论为根评论
     */
    public static final int COMMENT_STATUS_NORMAL = -1;

    //套餐图片所有图片名称
    public static final String Article_PIC_RESOURCES = "articlePicResources";
    //套餐图片保存在数据库中的图片名称
    public static final String Article_PIC_DB_RESOURCES = "articlePicDbResources";

    //菜单
    public static final String MENU="C";
    //按钮
    public static final String BUTTON="F";
    //管理员
    public static final String ADMIN = "1";
}