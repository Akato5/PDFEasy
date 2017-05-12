    var myApp = angular.module('fupApp', []);

    myApp.controller('fupController', function ($scope) {

        // GET THE FILE INFORMATION.
        $scope.getFileDetails = function (e) {

            $scope.files = [];
            $scope.$apply(function () {
                // STORE THE FILE OBJECT IN AN ARRAY.
                for (var i = 0; i < e.files.length; i++) {
                    $scope.files.push(e.files[i]);					
                }

            });
        };
		
        $scope.removeAll = function () {

                $scope.width="0%";
				$scope.files = null;
				file.value = null;
				accion.value = "";
				literal.value = "";
				if(this.formData != undefined && this.formData != null && this.formData.pdfopcion != undefined)
				{
					this.formData.pdfopcion = "";
					this.formData.literal = "";
				}
        }		

        // NOW UPLOAD THE FILES.
        $scope.uploadFiles = function () {			
            if($scope.files != undefined && $scope.files != null && $scope.formData != undefined && $scope.formData != undefined && $scope.formData.pdfopcion != undefined && $scope.formData.pdfopcion != null && $scope.formData.pdfopcion != ""){
				//FILL FormData WITH FILE DETAILS.
				var data = new FormData();

				for (var i in $scope.files) {
					data.append($scope.files[i].name, $scope.files[i]);
				}
				data.append("accion",$scope.formData.pdfopcion);
				
				var objXhr = new XMLHttpRequest();
				objXhr.responseType = 'arraybuffer';
				// ADD LISTENERS.
				objXhr.addEventListener("progress", updateProgress, false);
				objXhr.addEventListener("load", transferComplete, false);

				// SEND FILE DETAILS TO THE API.
				objXhr.open("POST", "/PDFEasy/upload");
				objXhr.send(data);
			}else{
				
				alert("Debe elegir una opción y seleccionar al menos un fichero");
			}
        }

        // UPDATE PROGRESS BAR.
        function updateProgress(e) {
            if (e.lengthComputable) {
                //document.getElementById('pro').setAttribute('value', e.loaded);
                //document.getElementById('pro').setAttribute('max', e.total);
				//barraprog.val() = (e.loaded*100)/e.total;
				//var porcentaje = (e.loaded*100)/e.total;
				var porcentaje = Math.round(e.loaded * 100 / e.total);
				$scope.$apply(function () {
					$scope.width=""+porcentaje+"%";
				});
				
				
				
            }
        }

        // CONFIRMATION.
        function transferComplete(e) {
			if (e.target.status === 200) {
					try{
						var filename = "";
						var disposition = e.target.getResponseHeader('Content-Disposition');
						if (disposition && disposition.indexOf('attachment') !== -1) {
							var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
							var matches = filenameRegex.exec(disposition);
							if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
						}
						var type = e.target.getResponseHeader('Content-Type');

						var blob = new Blob([e.target.response], { type: type });
						if(blob && filename != null && filename != "" && type != null ){
							if (typeof window.navigator.msSaveBlob !== 'undefined') {
								// IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
								window.navigator.msSaveBlob(blob, filename);
							} else {
								var URL = window.URL || window.webkitURL;
								var downloadUrl = URL.createObjectURL(blob);
								debugger;
								if (filename) {
									// use HTML5 a[download] attribute to specify filename
									var a = document.createElement("a");
									// safari doesn't support this yet
									if (typeof a.download === 'undefined') {
										window.location = downloadUrl;
									} else {
										a.href = downloadUrl;
										a.download = filename;
										document.body.appendChild(a);
										a.click();
									}
								} else {
									window.location = downloadUrl;
								}

								setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
							}
						}else{
							alert("Se ha producido un error. Intentelo de nuevo");		
						}
				}catch(err){
					alert("Se ha producido un error. Intentelo de nuevo");
				}
			}			
			
        }
    });


 
