package com.daoying.export.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 导出配置
 * @author zyp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "export_config")
public class ExportConfig {
    @Id
    @GeneratedValue
    public Integer id;

    public Integer taskId;

    @OneToMany(mappedBy = "exportConfig")
    public List<ExportFieldConfig> fieldConfigs;

    public Date createTime;

    public Date updateTime;

}
