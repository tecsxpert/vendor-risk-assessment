import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateVendor() throws Exception {
        String vendorJson = "{\"name\":\"TestVendor\",\"contactEmail\":\"test@vendor.com\",\"riskLevel\":\"LOW\"}";

        mockMvc.perform(post("/api/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestVendor"));
    }
}
