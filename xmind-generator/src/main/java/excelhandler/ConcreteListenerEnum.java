package excelhandler;

import lombok.Getter;

@Getter
public enum ConcreteListenerEnum {
    BANK("银行"),
    GENERAL("通用");

    private String desc;
    ConcreteListenerEnum(String desc){
        this.desc = desc;
    }

}
