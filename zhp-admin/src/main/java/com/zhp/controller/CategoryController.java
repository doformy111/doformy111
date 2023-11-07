package com.zhp.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.Utils.WebUtils;
import com.zhp.domain.dto.TagListDto;
import com.zhp.domain.entity.Category;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.CategoryVo;
import com.zhp.domain.vo.adminVo.ExcelCategoryVo;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.service.CategoryService;
import com.zhp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @GetMapping("/listAllCategory")
    public ResponseResult getAllCategory(){
        return tagService.getAllCategory();
    }
    @GetMapping("/list")
    public ResponseResult showAllCategory(Integer pageNum,Integer pageSize, String name,String status){
        return categoryService.showAllCategory(pageNum,pageSize,name,status);
    }
    @PostMapping
    public  ResponseResult addCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.addCategory(categoryVo);
    }
    @GetMapping("/{id}")
    public ResponseResult getOneCategory(@PathVariable("id") Long id){
        return categoryService.getOneCategory(id);
    }
    @PutMapping
    public  ResponseResult updateCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.updatCategory(categoryVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);

    }
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
