function fn_submit(){
	var fn = document.frm;
	
	if (fn.custname.value ==""){ //input에 값이 빈값인지 체크
		alert("회원성명이 입력되지 않았습니다.");
		fn.custoname.focus(); //포커싱주기
		return false; //함수끝내기
	}
	if (fn.phone.value ==""){ //input에 값이 빈값인지 체크
		alert("회원전화가 입력되지 않았습니다.");
		fn.phone.focus(); //포커싱주기
		return false; //함수끝내기
	}
	if (fn.address.value ==""){ //input에 값이 빈값인지 체크
		alert("회원주소가 입력되지 않았습니다.");
		fn.address.focus(); //포커싱주기
		return false; //함수끝내기
	}
	if (fn.joindate.value ==""){ //input에 값이 빈값인지 체크
		alert("가입일자가 입력되지 않았습니다.");
		fn.joindate.focus(); //포커싱주기
		return false; //함수끝내기
	}
	if (fn.grade.value ==""){ //input에 값이 빈값인지 체크
		alert("고객등급이 입력되지 않았습니다.");
		fn.grade.focus(); //포커싱주기
		return false; //함수끝내기
	}
	if (fn.city.value ==""){ //input에 값이 빈값인지 체크
		alert("도시코드가 입력되지 않았습니다.");
		fn.city.focus(); //포커싱주기
		return false; //함수끝내기
	}
	fn.submit(); //컨트롤러(서버)에 전송
}