package com.example.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
//@Setter
@RequiredArgsConstructor //필요한 생성자 자동생성
public class HelloLombok {
   // private String hello; //클래스에 속성 추가
   // private int lombok;

    //final을 붙여주면서 생성자로 만듦 (값을 변경할 수 없게 되면서 setter을 쓸 수 없다.)
    private final String hello;
    private final int lombok;

    /**
     * 생성자 직접 작성 코드
     * public HelloLombok(String hello, int lombok) {
     *     this.hello = hello;
     *     this.lombok = lombok;
     * }
     * */
    public static void main(String[] args) {
        // HelloLombok helloLombok = new HelloLombok();
        //helloLombok.setHello("헬로");
        //helloLombok.setLombok(5);

        //RequiredArgsConstructor을 통한 생성자 생성
        HelloLombok helloLombok = new HelloLombok("헬로", 5);

        System.out.println(helloLombok.getHello());
        System.out.println(helloLombok.getLombok());
    }

}
