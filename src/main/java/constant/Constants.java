package constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String MEDIA_TYPE_JSON = "application/json";
    public static final String REQUEST_FROM_FRONTEND = "Request Value Caught from Front-end ${body}, Content-Type: ${header.Content-Type}";
    public static final String AFTER_PROCESSOR_VALUE = "After processor - Response body: ${body}, Status: ${header.CamelHttpResponseCode}, Content-Type: ${header.Content-Type}";
    public static final String FINAL_RESPONSE_VALUE = "Response to Front-end ${body}, Content-Type: ${header.Content-Type}";
    public static final String JWT_TOKEN_PARSER = "jwtTokenParser";
    public static final String TOKEN_FROM_USERID = "Extracted UserId from JWT Token, UserID: ${exchangeProperty.userId}";
    public static final String EMPTY_STRING = "";
    public static final String ONE_SPACE = " ";
    public static final String PASSWORD_CHANGE_DONE_ROUTE = "direct:sendPasswordChangeDoneAckMail";
}
