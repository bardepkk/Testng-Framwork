package TestComponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{
	int max=1;
	int count=0;
	@Override
	public boolean retry(ITestResult result) {
		if(count<max) {
			count++;
			return true;
		}
		return false;
	}
	
	

}
