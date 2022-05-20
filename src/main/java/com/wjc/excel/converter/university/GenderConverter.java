package com.wjc.excel.converter.university;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * @author 王建成
 * @date 2022/5/19--9:09
 */
public class GenderConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 将excel中的性别数据转换为java数据
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String cellStr = context.getReadCellData().getStringValue();
        if (StrUtil.isEmpty(cellStr)){
            return null;
        }
        if (cellStr.equals("男")){
            return "002";
        }else if (cellStr.equals("女")){
            return "003";
        }else {
            return null;
        }

    }

    /**
     * 将java对象属性转换成excel
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        String cellValue = context.getValue();
        if (StrUtil.isEmpty(cellValue)){
            return new WriteCellData<>("");
        }
        if (cellValue.equals("002")){
            return new WriteCellData<>("男");
        }else if (cellValue.equals("003")){
            return new WriteCellData<>("女");
        }else {
            return null;
        }

    }
}
