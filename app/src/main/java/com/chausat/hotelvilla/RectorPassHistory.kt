package com.chausat.hotelvilla

data class RectorPassHistory(var Id: Int, var studentId: Int,
                             var studentName: String, var hostelNo: Int,
                             var roomNo: Int, var passType: Int,
                             var data1: String, var data2: String,
                             var destination: String, var purpose: String, val isRectorAccepted : Int, val isFacultyAccepted : Int,
                             val isCanceledAccepted : Int)