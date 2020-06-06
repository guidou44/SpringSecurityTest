package com.ken3d.threedfy.presentation.printer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterCluster;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterEntityBase;
import com.ken3d.threedfy.presentation.user.IUserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class IPrinterServiceTest {

  private static final User USER = new User();

  private final IEntityRepository<PrinterEntityBase> printerRepository = mock(
      IEntityRepository.class);
  private final IUserService userService = mock(IUserService.class);

  private List<PrinterCluster> printerClusterTable = new ArrayList<>();


  @BeforeEach
  void setUp() {
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      PrinterCluster printerCluster = (PrinterCluster) args[0];
      printerClusterTable.add(printerCluster);
      return printerCluster;
    }).when(printerRepository).create(any(PrinterCluster.class));

    willReturn(givenOrganizationWithNoClusters(USER)).given(userService)
        .getCurrentUserLoggedOrganization();
  }


  @Test
  public void givenMockDataBase_whenCreatePrinterCluster_itCreatesProperly() {
    IPrinterService service = givenPrinterService(printerRepository, userService);
    PrinterCluster cluster = new PrinterCluster();

    service.createPrinterCluster(cluster);

    assertThat(printerClusterTable).contains(cluster);
  }

  @Test
  public void givenCreatePrinterClusterForOrganization_whenGetOrganization_thenItReturnsProperOrg() {
    IPrinterService service = givenPrinterService(printerRepository, userService);
    PrinterCluster cluster = new PrinterCluster();

    service.createPrinterCluster(cluster);

    assertThat(cluster.getOrganization()).isEqualTo(userService.getCurrentUserLoggedOrganization());
  }

  @Test
  public void givenOrganizationWithoutPrinterClusters_whenGetPrinterClusters_itReturnsEmpty() {
    IPrinterService service = givenPrinterService(printerRepository, userService);

    List<PrinterCluster> clusters = service.getAllPrinterClustersForCurrentOrganization();

    assertThat(clusters).isEmpty();
  }

  @Test
  public void givenOrganizationWithMultiplePrinterClusters_whenGetClusters_itReturnsProperClusters() {
    IPrinterService service = givenPrinterService(printerRepository, userService);
    willReturn(givenOrganizationWithClusters(USER)).given(userService)
        .getCurrentUserLoggedOrganization();

    List<PrinterCluster> clusters = service.getAllPrinterClustersForCurrentOrganization();

    assertThat(clusters).isNotEmpty();
    assertThat(clusters)
        .allMatch(c -> c.getOrganization().equals(givenOrganizationWithClusters(USER)));
  }

  private Organization givenOrganizationWithNoClusters(User owner) {

    Organization org = new Organization();
    org.setOwner(owner);
    org.setId(1);
    org.setCollaborative(false);

    return org;
  }

  private Organization givenOrganizationWithClusters(User owner) {

    Organization org = givenOrganizationWithNoClusters(owner);
    org.setId(org.getId() + 1);

    Set<PrinterCluster> clusters = new HashSet<>(Arrays.asList(
        givenClusterForOrganization(org),
        givenClusterForOrganization(org),
        givenClusterForOrganization(org)
    ));

    org.setPrinterClusters(clusters);

    return org;
  }

  private PrinterCluster givenClusterForOrganization(Organization org) {
    PrinterCluster cluster = new PrinterCluster();
    cluster.setOrganization(org);
    cluster.setName("TEST");
    return cluster;
  }


  protected abstract IPrinterService
  givenPrinterService(IEntityRepository<PrinterEntityBase> repository, IUserService userService);

}
