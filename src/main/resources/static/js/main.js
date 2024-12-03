function showOrHideGrade(gradeType) {
  if (gradeType === "math") {
    var x = document.getElementById("mathGrade");
    if (x.style.display === "none") {
      x.style.display = "block";
    } else {
      x.style.display = "none";
    }
  }
  if (gradeType === "science") {
    var x = document.getElementById("scienceGrade");
    if (x.style.display === "none") {
      x.style.display = "block";
    } else {
      x.style.display = "none";
    }
  }
  if (gradeType === "history") {
    var x = document.getElementById("historyGrade");
    if (x.style.display === "none") {
      x.style.display = "block";
    } else {
      x.style.display = "none";
    }
  }
}

function deleteResource(path) {
  const form = document.getElementById('deleteForm');
  form.setAttribute('action', path);
  form.submit();
}

function deleteMathGrade(gradeId) {
  deleteResource(`/grades/MATH/${gradeId}`);
}

function deleteScienceGrade(gradeId) {
  deleteResource(`/grades/SCIENCE/${gradeId}`);
}

function deleteHistoryGrade(gradeId) {
  deleteResource(`/grades/HISTORY/${gradeId}`);
}

function studentInfo(id) {
  window.location.href = "/student-information/" + id;
}

function deleteStudent(studentId) {
  deleteResource(`/${studentId}`);
}
