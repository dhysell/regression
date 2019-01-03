package persistence.guidewire.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.*;
import persistence.guidewire.entities.ExistingPolicyLookUp;
import persistence.helpers.DateUtils;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingPoliciesHelper {
	
	public static ArrayList<ExistingPolicyLookUp> getRandomPolicyByType(String environment, PolicyType product,PolicyLineType Type, TransactionType jobtype, Date closedate, SqlSigns sign) throws Exception {	
		String myDate = new SimpleDateFormat("yyyy-MM-dd").format(closedate);
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
			
			switch (environment) {
				case "DEV" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8DEV);
					break;
				case "IT" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8IT);
					break;
				case "QA" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8QA);
					break;
				case "UAT" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8UAT);
					break;
				case "REGR01" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8REGR01);
					break;
				case "REGR02" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8REGR02);
					break;
				case "REGR03" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8REGR03);
					break;
				case "REGR04" :
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewirePC8REGR04);
					break;
				default:
					throw new Exception("ERROR: The environment \"" + environment + "\" does not correlate to a Hibernate config file so nothing can work until this is resolved.");
			}
			
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			@SuppressWarnings("unchecked")
			List<Object[]> rows = (List<Object[]>)em.createNativeQuery("SELECT * FROM (SELECT DISTINCT ACCT.AccountNumber,jb.JobNumber,"
					+ "pp.PolicyNumber,pp.TermNumber,PP.PeriodStart,PP.PeriodEnd,jb.CloseDate,PTJ.L_en_US [JobBType],POL.ProductCode,"
					+ "PL.PatternCode,PC.Code,RANK() OVER (PARTITION BY PP.PolicyNumber ORDER BY  (cast( pp.TERMNUMBER as varchar(3))"
					+ "  + A.JOB_RANK + CONVERT(varchar(8), pp.PeriodStart, 112) + cast(JB.JOBNUMBER as varchar(20)))   DESC  ) as [rank] "
					+ "FROM (SELECT JB.JobNumber, PolicyNumber, TermNumber, CASE WHEN PTJ.L_en_US = 'Cancellation' AND pp.MostRecentModel"
					+ " ='1' THEN '5' WHEN PTJ.L_en_US = 'PolicyChange' AND pp.CancellationDate IS NULL THEN '4' WHEN PTJ.L_en_US = "
					+ "'Reinstatement' AND pp.CancellationDate IS NULL THEN '4' WHEN PTJ.L_en_US = 'PolicyChange' AND pp.CancellationDate "
					+ "IS NOT NULL THEN '0' ELSE '1' END [JOB_RANK] FROM pc_policyperiod PP WITH(NOLOCK) INNER JOIN pc_job JB WITH(NOLOCK)"
					+ "ON PP.JobID = JB.ID INNER JOIN DBO.pctl_job PTJ WITH(NOLOCK) ON JB.Subtype = PTJ.ID WHERE pp.Status = 9 and pp.Retired"
					+ " = 0) A JOIN  pc_policyperiod PP WITH(NOLOCK) ON A.PolicyNumber = PP. PolicyNumber AND A.TermNumber = PP.TermNumber "
					+ "INNER JOIN pc_policy POL WITH(NOLOCK) ON PP.POLICYID = POL.ID INNER JOIN pc_job JB WITH(NOLOCK) ON A.JobNumber = "
					+ "JB.JobNumber INNER JOIN pctl_job PTJ WITH(NOLOCK) ON JB.SUBTYPE = PTJ.ID INNER JOIN pc_policyline PL WITH(NOLOCK) ON "
					+ "PP.ID = PL.BranchID INNER JOIN pc_Producercode PC WITH(NOLOCK) ON POL.ProducerCodeOfServiceID = PC.ID INNER JOIN "
					+ "pc_account ACCT WITH(NOLOCK) ON POL.AccountID = ACCT.ID WHERE pp.Status = 9) b WHERE b.[rank] = 1 and JobBType = '" 
					+ jobtype + "' and ProductCode = '" + product + "' and PatternCode = '" + Type.getDBValue() + "' and Closedate " + 
					sign.getDBValue() + " '" + myDate + "'").getResultList();
			
			ArrayList<ExistingPolicyLookUp> listToReturn = new ArrayList<ExistingPolicyLookUp>();
			for(Object[] row : rows){
				ExistingPolicyLookUp epl = new ExistingPolicyLookUp();
				epl.setAccountNumber(row[0].toString());
				epl.setpolicyTransactionJob(row[1].toString());
				epl.setPolicyNumber(row[2].toString());
				epl.setPolicyPeriodTermNumber(Integer.parseInt(row[3].toString()));
				//System.out.println(row[4].toString().substring(0, 10));
				epl.setperiodStart(DateUtils.convertStringtoDate(row[4].toString(), "yyyy-MM-dd"));
				//System.out.println(row[5].toString());
				epl.setperiodEnd(DateUtils.convertStringtoDate(row[5].toString(), "yyyy-MM-dd"));
				epl.setcloseDate(DateUtils.convertStringtoDate(row[6].toString(), "yyyy-MM-dd"));
				epl.setpolicyLine(row[9].toString());
				//System.out.println(row[8].toString());
				epl.setcode(Integer.parseInt(row[10].toString()));
				listToReturn.add(epl);
				//System.out.println(epl);
			}
			
			session.getTransaction().commit();
            
            return listToReturn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
