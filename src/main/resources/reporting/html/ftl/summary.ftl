<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Ukwikora - Dashboard</title>

  <!-- Custom fonts for this template-->
  <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="vendor/googleapis/fonts.css" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="css/dashboard.css" rel="stylesheet">

</head>

<body id="page-top">

  <!-- Page Wrapper -->
  <div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

      <!-- Sidebar - Brand -->
      <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
        <div class="sidebar-brand-icon">
          <i class="fas fa-android"></i>
        </div>
        <div class="sidebar-brand-text mx-3">Ukwikora</div>
      </a>

      <!-- Divider -->
      <hr class="sidebar-divider my-0">

      <!-- Dashboards Accordion Menu -->
      <li class="nav-item active">
        <a class="nav-link" href="#" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
          <i class="fas fa-fw fa-tachometer-alt"></i>
          <span>Dashboards</span>
        </a>
        <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordionSidebar">
          <div class="bg-white py-2 collapse-inner rounded">
            <h6 class="collapse-header">Dashboard</h6>
            <a class="collapse-item" href="index.html">Dependencies</a>
            <a class="collapse-item" href="#">Summary</a>
          </div>
        </div>
      </li>

      <!-- Divider -->
      <hr class="sidebar-divider">

      <!-- Heading -->
      <div class="sidebar-heading">
        Detailed View
      </div>

      <!-- Nav Item - Pages Collapse Menu -->
      <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
          <i class="fas fa-fw fa-folder"></i>
          <span>Projects</span>
        </a>
        <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
          <div class="bg-white py-2 collapse-inner rounded">
            <#list summary.links as link>
            <a class="collapse-item" href="${link.url}.html">${link.text}</a>
            </#list>a>
          </div>
        </div>
      </li>

      <!-- Divider -->
      <hr class="sidebar-divider">

      <!-- Heading -->
      <div class="sidebar-heading">
        Addons
      </div>

      <!-- Nav Item - Charts -->
      <li class="nav-item">
        <a class="nav-link" href="charts.html">
          <i class="fas fa-fw fa-chart-area"></i>
          <span>Clones</span></a>
      </li>

      <!-- Nav Item - Tables -->
      <li class="nav-item">
        <a class="nav-link" href="tables.html">
          <i class="fas fa-fw fa-table"></i>
          <span>Dead Code</span></a>
      </li>

      <!-- Divider -->
      <hr class="sidebar-divider d-none d-md-block">

      <!-- Sidebar Toggler (Sidebar) -->
      <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
      </div>

    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content">

        <!-- Topbar -->
        <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

          <!-- Topbar Search -->
          <form class="d-none d-md-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 w-100">
            <div class="input-group">
              <input type="text" class="form-control border-0 bg-light" placeholder="Search for..." aria-label="Search" aria-describedby="basic-addon2" style="font-size: 0.85rem;">
              <div class="input-group-append">
                <button class="btn btn-primary" type="button">
                  <i class="fas fa-search fa-sm"></i>
                </button>
              </div>
            </div>
          </form>

          <!-- Topbar Navbar -->
          <ul class="navbar-nav mr-auto ml-md-auto">

          </ul>

          <!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none">
            <i class="fa fa-bars"></i>
          </button>

        </nav>
        <!-- End of Topbar -->

        <div class="container-fluid">

          <!-- Area Chart -->
          <div class="card border-0 shadow mb-4">
            <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
              <h6 class="m-0 font-weight-bold text-primary">Lines of code per project</h6>
            </div>
            <div class="card-body">
              <div class="chart-area">
                <canvas id="locChart"></canvas>
              </div>
            </div>
          </div>

          <!-- Area Chart -->
          <div class="card border-0 shadow mb-4">
            <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
              <h6 class="m-0 font-weight-bold text-primary">Test cases per project</h6>
            </div>
            <div class="card-body">
              <div class="chart-area">
                <canvas id="testCasesChart"></canvas>
              </div>
            </div>
          </div>

          <!-- Area Chart -->
          <div class="card border-0 shadow mb-4">
            <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
              <h6 class="m-0 font-weight-bold text-primary">User keyword per project</h6>
            </div>
            <div class="card-body">
              <div class="chart-area">
                <canvas id="userKeywordsChart"></canvas>
              </div>
            </div>
          </div>

        </div>
        <!-- /.container-fluid -->

      </div>
      <!-- End of Main Content -->

      <!-- Footer -->
      <footer class="sticky-footer bg-white">
        <div class="container my-auto">
          <div class="copyright text-center my-auto">
            <span>Copyright &copy; Ukwikora 2019</span>
          </div>
        </div>
      </footer>
      <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

  </div>
  <!-- End of Page Wrapper -->

  <!-- Scroll to Top Button-->
  <a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
  </a>

  <!-- Bootstrap core JavaScript-->
  <script src="vendor/jquery/jquery.min.js"></script>
  <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Core plugin JavaScript-->
  <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Custom scripts for all pages-->
  <script src="js/dashboard.js"></script>

  <!-- Page level plugins -->
  <script src="vendor/chart.js/Chart.min.js"></script>

  <!-- Page level custom scripts -->
  <script src="js/summary-loc.js"></script>
  <script src="js/summary-test-cases.js"></script>
  <script src="js/summary-user-keywords.js"></script>
</body>

</html>
