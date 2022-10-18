
public class CustomerDTO {
	private String idx;
	private String name;
	private String age;
	private String eMail;
	private String jumin;
	
	// 기본 생성자, 파라미터 5개 (모든 멤버변수) 초기화 생성자
	public CustomerDTO() {}

	public CustomerDTO(String idx, String name, String age, String eMail, String jumin) {
		this.idx = idx;
		this.name = name;
		this.age = age;
		this.eMail = eMail;
		this.jumin = jumin;
	}

	// Getter / Setter
	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getJumin() {
		return jumin;
	}

	public void setJumin(String jumin) {
		this.jumin = jumin;
	}
	
	
	
}
