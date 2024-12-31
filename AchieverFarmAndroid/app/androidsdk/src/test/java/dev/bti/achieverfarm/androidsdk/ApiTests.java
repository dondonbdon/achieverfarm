package dev.bti.achieverfarm.androidsdk;

import org.junit.Test;

import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.models.ItemOptions;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ApiTests {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoV3RuMzExT0ZVaU0xT2o3Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTczMzI0MDE4NywiZXhwIjoxNzM1ODMyMTg3fQ.rNv-I9zKZlMgBjWbH6FwWt_2Vc-Oh6rUZn7sfE_KMnY";

    @Test
    public void apiTest() {
        SDKHelpers.GetInstance(token).getItemHelper().getItemsWithOptions(new ItemOptions())
                .addOnSuccessListener(System.out::println)
                .addOnFailureListener(System.out::println)
                .execute();
    }

    @Test
    public void getUser() {
        System.out.println("?");

        SDKHelpers.GetInstance(token).getUserHelper().getUser("hWtn311OFUiM1Oj7")
                .addOnSuccessListener(x -> {
                    System.out.println("SUCCESS");
                    System.out.println(x);
                })
                .addOnFailureListener(x1 -> {
                    System.out.println("ERROR");
                    x1.fillInStackTrace();
                }).execute();
    }


}