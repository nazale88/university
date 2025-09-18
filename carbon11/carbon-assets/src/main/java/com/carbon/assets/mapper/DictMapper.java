package com.carbon.assets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.entity.CarbonAssets;
import com.carbon.assets.param.CarbonAssetsQueryParam;
import com.carbon.assets.vo.CarbonAssetsQueryVo;
import com.carbon.domain.assets.vo.DictVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典编码
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Repository
public interface DictMapper{
    String getDictCh(@Param("dictCode") String dictCode);

    @Select("SELECT item_value as code, item_ch as name FROM sys_dict_item WHERE dict_code = #{parentCode}")
    List<DictVo> getDictListByCode(@Param("parentCode") String parentCode);
}
