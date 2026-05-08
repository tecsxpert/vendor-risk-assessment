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
class VendorControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/vendors"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="apoorva", roles={"ADMIN"})
    void testAuthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/vendors"))
               .andExpect(status().isOk());
    }
}
