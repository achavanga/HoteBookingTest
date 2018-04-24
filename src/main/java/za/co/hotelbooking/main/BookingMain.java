
package za.co.hotelbooking.main;

import java.io.File;
import java.util.Collections;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;


public class BookingMain {

    /**
     * @param args the command line arguments
     * @throws org.apache.maven.shared.invoker.MavenInvocationException
     */
    public static void main(String[] args) throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("pom.xml"));
        request.setGoals(Collections.singletonList("install"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("M2_HOME")));
//        try {
            invoker.execute(request);
//        } catch (MavenInvocationException e) {
//            e.printStackTrace();
//        }
    }
}
