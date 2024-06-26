package com.example.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//DB에서 특정 엔티티 또는 데이터를 찾을 수 없을 때 발생시키는 사용자 정의 예외 클래스
//@ResponseStatus : 해당 예외 발생시 반환될 응답 코드와 메시지 지정
//HTTP 상태코드와 메시지(reason = "~")를 포함한 응답을 생성하여 클라이언트에게 반환
//RuntimeException 상속 : 실행 시 발생하는 예외,
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
    //직렬화 역직렬화에 필요한 부분인데 이클립스에서만 필요하다고 함..아마도..
    /**
     * 직렬화 : 객체를 데이터 스트림으로 변환하는 과정
     *      -> 객체의 상태를 바이트 스트림으로 변환하여 파일에 저장하거나
     *          네트워크를 통해 전송할 수 있음
     *      -> 역직렬화를 통해 원래 객체로 복원 가능
     *
 *      serialVersionUID(직렬화 버전 번호):
     *      - 직렬화된 데이터와 역직렬화하려는
     *          클래스의 버전이 일치하는지 확인할 수 있는 고유번호?
     *      - 직렬화된 클래스의 구조가 변경되었을 때 버전을 업데이트하여
     *          호환성을 유지할 수 있다.
     */
    private static final long serialVersionUID = 1L; //클래스의 직렬화 버전 번호
    public DataNotFoundException(String message) {
        super(message);
    }
}
