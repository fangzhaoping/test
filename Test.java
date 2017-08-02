import java.rmi.RemoteException;

import org.apache.axis.AxisProperties;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.liana.base.Trace;
import com.ecc.liana.examineeNetPay.base.ENPConstants;
import com.examineeNetPay.util.DESCoder;
import com.todaytech.gfmis.webservices.face.ExternalInterfaceWSProxy;
import com.todaytech.gfmis.webservices.vo.LoginRetVO;
import com.todaytech.gfmis.webservices.vo.LoginVO;
import com.todaytech.gfmis.webservices.vo.PayNoteVO;
import com.todaytech.gfmis.webservices.vo.SearchPayNoteListRetVO;
import com.todaytech.gfmis.webservices.vo.SearchPayNoteVO;


public class Test {
	
	/**
	 * 登录信息码
	 */
	private static String loginKey = "w|CITY";
	
	private static String wsdlURL4Test = "http://203.91.43.13:8010/ttfsws/services/ExternalInterfaceWS";
	//private static String wsdlURL4Product = "http://203.91.43.13:7001/ttfsws/services/ExternalInterfaceWS";
	//private static String wsdlURL4Product = "http://203.91.43.13:8010/ttfsws/services/ExternalInterfaceWS";
	private static String wsdlURL4Product = "https://fs.szfb.gov.cn/ttfsws/services/ExternalInterfaceWS";
	
	public static void main(String[] args) throws RemoteException, EMPException{
		System.out.println("111111111111111111111");
//		System.setProperty("http.proxyHost", "10.14.52.41");
//		System.setProperty("http.proxyPort", "50005");
		//System.setProperty("http.proxyHost", "10.14.63.226");
		//System.setProperty("http.proxyPort", "80");
		System.setProperty("http.proxyHost", "10.2.5.83");
		System.setProperty("http.proxyPort", "8080");
		AxisProperties.setProperty("axis.socketSecureFactory","com.todaytech.gfmis.webservices.face.MyCustomSSLSocketFactory");
		
		ExternalInterfaceWSProxy wsProxy = new ExternalInterfaceWSProxy();
		wsProxy.setEndpoint(wsdlURL4Product); 
		//sendMessageSearchPayNote(wsProxy);
		sendMessageLogin(wsProxy);
	}
	
	/**
	 * 登录
	 * @param context
	 * @param wsProxy
	 * @throws RemoteException
	 * @throws EMPException 
	 */
	private static void sendMessageLogin(ExternalInterfaceWSProxy wsProxy) throws RemoteException, EMPException {
		// TODO Auto-generated method stub
		LoginRetVO loginRet = null;
		LoginVO loginvo = new LoginVO();
		
		loginvo.setUnitNO("014006");
		loginvo.setPassword("123456");
		
		loginRet = wsProxy.login(loginvo);
		String retCode = loginRet.getReturnCode();
		if("100".equals(retCode)){
			System.out.println(loginvo.getUnitNO() + "登录成功！");
		}else{
			Trace.log(ENPConstants.ENP_COMMONLOG, "登录失败，错误码：" + retCode 
					+"；错误原因：" + loginRet.getReason());
//				System.out.println("登录失败，错误码：" + retCode 
//						+"；错误原因：" + loginRet.getReason());
			throw new RemoteException();
		}
		
		
	}
	
	/**
	 * 查询缴款信息
	 * @param context
	 * @param wsProxy
	 * @throws RemoteException 
	 * @throws EMPException 
	 */
	private static void sendMessageSearchPayNote(ExternalInterfaceWSProxy wsProxy) throws RemoteException, EMPException {
		// TODO Auto-generated method stub
		SearchPayNoteListRetVO searchPayNoteListRet = null;
		SearchPayNoteVO searchPayNote = new SearchPayNoteVO();
//		if("".equals(loginKey) || loginKey == null){
//			sendMessageLogin(context, wsProxy );
//		}
		try{
			
			searchPayNote.setLoginKey(loginKey);
			
			searchPayNote.setUnitNO("014006");
//			searchPayNote.setCreateDTBegin("");
			searchPayNote.setStatus(4);//查询所有状态的数据
			searchPayNote.setPageNo(1);
//			searchPayNote.setPnNOBegin((String)context.getDataValue("payNoticeNo"));
//			searchPayNote.setPnNOEnd((String)context.getDataValue("payNoticeNo"));
			searchPayNote.setPnNOBegin("0151300000214109");
			searchPayNote.setPnNOEnd("0151300000214109");
			
			searchPayNoteListRet = wsProxy.searchPayNote(searchPayNote);
			String retCode = searchPayNoteListRet.getReturnCode();
//			context.setDataValue("returnCode", retCode);
			if("100".equals(retCode)){
				System.out.println(searchPayNoteListRet.getPayNoteCount() + "条查询成功！");
				String payNotes = searchPayNoteListRet.getPayNotes();
				Trace.log(ENPConstants.ENP_COMMONLOG, "查询返回数据：" + payNotes);
				System.out.println(payNotes);
				PayNoteVO [] payNoteVOArr = PayNoteVO.getPaynotes(payNotes);
				String test = DESCoder.decode(payNoteVOArr[0].getReceivableAmt()) ;
				System.out.println(test);
//				IndexedCollection icoll = (IndexedCollection)context.getDataElement("payNoteInfo");
//				for (int i=0;i<payNoteVOArr.length;i++){
//					KeyedCollection kcoll = (KeyedCollection)icoll.getDataElement().clone();
//					kcoll.setDataValue("pnNO",payNoteVOArr[i].getPnNO() );
//					kcoll.setDataValue("status",String.valueOf(payNoteVOArr[i].getStatus()));
//					kcoll.setDataValue("receivableAmt",DESCoder.decode(payNoteVOArr[i].getReceivableAmt()) );
//					kcoll.setDataValue("receivedAmt",DESCoder.decode(payNoteVOArr[i].getReceivedAmt()) );
//					kcoll.setDataValue("rtNO",payNoteVOArr[i].getRtNO() );
//					kcoll.setDataValue("receiptNO",payNoteVOArr[i].getReceiptNO() );
//					kcoll.setDataValue("paidDT",payNoteVOArr[i].getPaidDT() );
//					kcoll.setDataValue("bankName",payNoteVOArr[i].getBankName() );
//					kcoll.setDataValue("payerName",payNoteVOArr[i].getPayerName() );
//					
//					icoll.add(kcoll);
				}
//				String[] str = payNotes.split("\\|");
//				System.out.println(DESCoder.decode(str[2]));
//			}else if("202".equals(retCode)){
				//死循环
//				sendMessageLogin(context, wsProxy );
//				sendMessageSearchPayNote(context, wsProxy ); 
//			}else{
//				System.out.println("查询失败，错误码：" + retCode 
//						+"；错误原因：" + searchPayNoteListRet.getReason());
//				throw new RemoteException();
//			}
//		}catch (ObjectNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
