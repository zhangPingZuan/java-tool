package excelhandler;

import lombok.*;

import java.util.List;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 16:46
 * @description
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelData {

    private String group;

    private String level;

    private String clientName;

    private String originLevel;

    private List<ExcelData>  children;

}
