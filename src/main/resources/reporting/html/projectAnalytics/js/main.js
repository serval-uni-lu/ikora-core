$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

    var firstValue = $("#pages div:first-child").attr("id");
    onClickMenu(firstValue);
});

function onClickMenu(value) {
    $( "#pages" ).children("div").filter(function(){
        return $( this ).attr( "id" ) == value;
    }).show(showPage(value));

    $( "#pages" ).children("div").filter(function(){
        return $( this ).attr( "id" ) != value;
    }).hide();
}

function showPage(pageId) {
    switch(pageId){
        case "statistics": loadStatistics(); break;
        case "clones": loadClones(); break;
        case "deadCode": loadDeadCode(); break;
    }
}

function loadStatistics(){
    var div = $("#statistics");
    div.text("Here is the statics page!");
}

function loadClones() {
    var div = $("#clones");
    div.text("Here is the clones page!");
}

function loadDeadCode() {
    var div = $("#deadCode");
    div.text("Here is the dead code page!");
}