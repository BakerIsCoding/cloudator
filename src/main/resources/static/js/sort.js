var sortStatus = [false, false, false, false, false];

        function sortTableAndToggleIcon(columnIndex, anchorElement) {
            sortTable(columnIndex);
            toggleIcon(anchorElement.querySelector('i'), columnIndex);
        }

        function toggleIcon(iconElement, columnIndex) {
            if (iconElement.classList.contains('mdi-chevron-down')) {
                iconElement.classList.remove('mdi-chevron-down');
                iconElement.classList.add('mdi-chevron-up');
                sortStatus[columnIndex] = true;
            } else {
                iconElement.classList.remove('mdi-chevron-up');
                iconElement.classList.add('mdi-chevron-down');
                sortStatus[columnIndex] = false;
            }
        }

        function sortTable(columnIndex) {
            var table, rows, switching, i, x, y, shouldSwitch;
            table = document.getElementById("fileTable");
            switching = true;
            while (switching) {
                switching = false;
                rows = table.getElementsByTagName("tr");
                for (i = 1; i < (rows.length - 1); i++) {
                    shouldSwitch = false;
                    x = rows[i].getElementsByTagName("td")[columnIndex];
                    y = rows[i + 1].getElementsByTagName("td")[columnIndex];
                    var shouldSwitchUp = sortStatus[columnIndex] && x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase();
                    var shouldSwitchDown = !sortStatus[columnIndex] && x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase();
                    if (shouldSwitchUp || shouldSwitchDown) {
                        shouldSwitch = true;
                        break;
                    }
                }
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                }
            }
        }