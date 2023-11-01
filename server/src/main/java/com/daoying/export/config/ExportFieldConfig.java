package com.daoying.export.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导出字段配置
 * @author zyp
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "export_field_config")
public class ExportFieldConfig {

    @Id
    @GeneratedValue
    public Integer id;

    /**
     * 字段
     */
    public String field;

    /**
     * 字段导出名称
     */
    public String fieldName;

    /**
     * 字段类型
     */
    @Enumerated(EnumType.STRING)
    public FieldType type = FieldType.STRING;

    /**
     * 字段处理脚本
     */
    public String script;

    /**
     * 排序
     */
    public Integer sort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    public ExportConfig exportConfig;

}
