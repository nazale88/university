package com.carbon.system.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.carbon.domain.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * <p>
 * 碳文章
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CarbonArticleParam", description="碳文章")
public class CarbonArticleParam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章简介")
    private String description;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "分类名字")
    private String categoryIdName;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "来源")
    private String copyFrom;

    @ApiModelProperty(value = "跳转url")
    private String url;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;
}
