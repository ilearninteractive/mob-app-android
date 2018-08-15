package sa.gov.moe.etraining.user;

import com.google.gson.annotations.SerializedName;

public enum DataType {
    @SerializedName("country")
    COUNTRY,

    @SerializedName("language")
    LANGUAGE,

    @SerializedName("boolean")
    BOOLEAN
}
