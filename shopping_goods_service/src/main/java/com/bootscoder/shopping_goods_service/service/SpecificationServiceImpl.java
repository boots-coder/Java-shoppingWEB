package com.bootscoder.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootscoder.shopping_common.pojo.Specification;
import com.bootscoder.shopping_common.pojo.SpecificationOption;
import com.bootscoder.shopping_common.pojo.SpecificationOptions;
import com.bootscoder.shopping_common.service.SpecificationService;
import com.bootscoder.shopping_goods_service.mapper.SpecificationMapper;
import com.bootscoder.shopping_goods_service.mapper.SpecificationOptionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@DubboService
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public void add(Specification specification) {
        specificationMapper.insert(specification);
    }

    @Override
    public void update(Specification specification) {
        specificationMapper.updateById(specification);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            // 删除商品规格项
            QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();
            queryWrapper.eq("specId",id);
            specificationOptionMapper.delete(queryWrapper);
            // 删除商品规格
            specificationMapper.deleteById(id);
        }
    }

    @Override
    public Specification findById(Long id) {
        return specificationMapper.findById(id);
    }

    @Override
    public Page<Specification> search(int page, int size) {
        return specificationMapper.selectPage(new Page(page,size),null);
    }

    @Override
    public List<Specification> findByProductTypeId(Long id) {
        return specificationMapper.findByProductTypeId(id);
    }

    @Override
    public void addOption(SpecificationOptions specificationOptions) {
        // 拿到规格id
        Long specId = specificationOptions.getSpecId();
        // 拿到规格项名数组
        String[] optionNames = specificationOptions.getOptionName();
        for (String optionName : optionNames) {
            // 构建规格项对象
            SpecificationOption specificationOption = new SpecificationOption();
            specificationOption.setSpecId(specId);
            specificationOption.setOptionName(optionName);
            // 存到数据库中
            specificationOptionMapper.insert(specificationOption);
        }
    }

    @Override
    public void deleteOption(Long[] ids) {
        specificationOptionMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
