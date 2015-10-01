package dto;

public class UserLogin {
      
	public boolean userLogin(String name){
		System.out.println("called");
		if(userExists(name)){
			return true;
		}else{
		return false;
		}
	}
	public boolean userExists(String name){
		System.out.println("come here");
		return false;
	}
}
