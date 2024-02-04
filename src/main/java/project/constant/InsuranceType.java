package project.constant;

import lombok.Getter;

@Getter
public enum InsuranceType {

    LIFE("Životní pojištění"),
    PROPERTY("Pojištění majetku"),
    TRAVEL("Cestovní pojištění"),
    ACCIDENT("Úrazové pojištění");

    private final String description;

    InsuranceType(String description) {
        this.description = description;
    }

}
