package MainPackage;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Container {

		Runtime rt;
		ContainerController container;
		static int AUnum=1000;
		static int AEnum=5;

		public ContainerController initContainerInPlatform(String host, String port, String containerName) {
			// Get the JADE runtime interface (singleton)
			this.rt = Runtime.instance();

			// Create a Profile, where the launch arguments are stored
			Profile profile = new ProfileImpl();
			profile.setParameter(Profile.CONTAINER_NAME, containerName);
			profile.setParameter(Profile.MAIN_HOST, host);
			profile.setParameter(Profile.MAIN_PORT, port);
			// create a non-main agent container
			ContainerController container = rt.createAgentContainer(profile);
			return container;
		}

		public void initMainContainerInPlatform(String host, String port, String containerName) {

			// Get the JADE runtime interface (singleton)
			this.rt = Runtime.instance();

			// Create a Profile, where the launch arguments are stored
			Profile prof = new ProfileImpl();
			prof.setParameter(Profile.CONTAINER_NAME, containerName);
			prof.setParameter(Profile.MAIN_HOST, host);
			prof.setParameter(Profile.MAIN_PORT, port);
			prof.setParameter(Profile.MAIN, "true");
			prof.setParameter(Profile.GUI, "true");

			// create a main agent container
			this.container = rt.createMainContainer(prof);
			rt.setCloseVM(true);

		}
		
		public static void setAECoords(int num,ArrayList<AEInfo> ael){
			Random random = new Random();
			boolean valid=false;
			int coords[]=new int[2];
			while(!valid){
				coords[0]=random.nextInt(100 - 0 + 1) + 0;
				coords[1]=random.nextInt(100 - 0 + 1) + 0;
				valid=true;
				if (!ael.isEmpty()){
					for (AEInfo a : ael){
						if (Math.sqrt((a.getCoords()[0]-coords[0])*(a.getCoords()[0]-coords[0])+(a.getCoords()[1]-coords[1])*(a.getCoords()[1]-coords[1]))<5) valid=false;
					}
				}
			}
			
			AEInfo aei= new AEInfo(num,coords[0],coords[1],5);		
			ael.add(aei);	
		}
		
		private void startAINTInPlatform(String name, String classpath, ArrayList<AEInfo> ael,ArrayList<AUInfo> aul) {
			try {
				AgentController ac = container.createNewAgent(name, classpath, new Object[]{ael,aul});
				ac.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void startAEInPlatform(AEInfo aei, String name, String classpath,ArrayList<AEInfo> ael) {
			double precoPorDist=2;
			HashMap<Integer,Double> distances=new HashMap<Integer,Double>();
			int coords[]=aei.getCoords();

			for (AEInfo a : ael){
				distances.put(a.getAENum(),Math.sqrt((a.getCoords()[0]-coords[0])*(a.getCoords()[0]-coords[0])+(a.getCoords()[1]-coords[1])*(a.getCoords()[1]-coords[1])));
			}		
			
			try {
				AgentController ac = container.createNewAgent(name, classpath, new Object[]{precoPorDist,aei,distances});
				ac.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void startAUInPlatform(String name, String classpath,HashMap<Double,AEInfo> incentivos,ArrayList<AEInfo> ael,ArrayList<AUInfo> aul) {
			Random random = new Random();
			int coords[]=new int[2];
			coords[0]=random.nextInt(100 - 0 + 1) + 0;
			coords[1]=random.nextInt(100 - 0 + 1) + 0;			
			
			AUInfo au=new AUInfo(coords,name);
			aul.add(au);
			try {
				AgentController ac = container.createNewAgent(name, classpath, new Object[]{au,ael,incentivos});
				ac.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		

		public static void main(String[] args) throws InterruptedException {
			Random random = new Random();
			ArrayList<AEInfo> ael;
			ArrayList<AUInfo> aul;			
			int num=AEnum;
			ael=new ArrayList<AEInfo>();
			aul=new ArrayList<AUInfo>();
			
			Container a = new Container();
			a.initMainContainerInPlatform("localhost", "9888", "Container");
			
			while(num>0){
				setAECoords(num,ael);
				num--;
			}
			
			for (AEInfo aei : ael) {
				a.startAEInPlatform(aei,"AE"+AEnum, "MainPackage.AgenteEstacao",ael);
				AEnum--;
			}			
			
			a.startAINTInPlatform("AINT", "MainPackage.AgenteInterface",ael,aul);
			int i=0;
			int waitTime;
				while (AUnum>0) {
					i++;
					waitTime=random.nextInt(8 - 2 + 1) + 2;
					HashMap<Double,AEInfo> incentivos=new HashMap<Double,AEInfo>();
					a.startAUInPlatform("AU"+i, "MainPackage.AgenteUtilizador",incentivos,ael,aul);
					AUnum--;
					TimeUnit.SECONDS.sleep(waitTime);
				}
		}


	
}
