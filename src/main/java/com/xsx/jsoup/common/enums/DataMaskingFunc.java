package com.xsx.jsoup.common.enums;

import com.xsx.jsoup.common.DataMaskingOperation;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/11:41
 * @Version: 1.0
 * @Discription:
 **/
public enum DataMaskingFunc {

    /**
     * 脱敏转换器
     */
    NO_MASK((str, maskChar) -> {
        return str;
    }),
    ALL_MASK((str, maskChar) -> {
        if (StringUtils.isNotBlank(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(StringUtils.isNotBlank(maskChar) ? maskChar : DataMaskingOperation.MASK_CHAR);
            }
            return sb.toString();
        } else {
            return str;
        }
    }),
    MOBLIE_MASK((str, maskChar) -> {
        if (StringUtils.isNotBlank(str)) {
            if (str.length() == 11) {
                StringBuilder builder = new StringBuilder();
                builder.append(str.substring(0, 3));
                for (int i = 3; i < 8; i++) {
                    builder.append(StringUtils.isNotBlank(maskChar) ? maskChar : DataMaskingOperation.MASK_CHAR);
                }
                builder.append(str.substring(8, str.length()));
                return builder.toString();
            } else {
                return str.replace(str, StringUtils.isNotBlank(maskChar) ? maskChar : DataMaskingOperation.MASK_CHAR);
            }
        }
        return str;
    });

    private final DataMaskingOperation operation;

    private DataMaskingFunc(DataMaskingOperation operation) {
        this.operation = operation;
    }

    public DataMaskingOperation operation() {
        return this.operation;
    }

}

