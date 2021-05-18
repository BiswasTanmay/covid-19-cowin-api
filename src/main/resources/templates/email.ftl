<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<style>
td{
	border: 1px solid #000000;
	padding: 8px;
}
tr {
	border: 1px solid #000000;
	padding: 8px;
}
</style>
</head>
<body>
<p>Hello,
<p>Vaccine is available in your area Pincode: <b>${Pincode}</b>.
<p>Please see the below table for more details.
<p>You may visit <a href="https://selfregistration.cowin.gov.in/">Cowin</a> to book your slot.
<#list availableList as centList>

<tr>
<td><b>Name: ${centList.name} </b></td>
<tr>
<td> <b>Address: ${centList.address} </be></td>
</tr>
<tr>
<td><b>Date</b></td>
<td><b>Seat</b></td>
<td><b>Dose 1</b></td>
<td><b>Dose 2</b></td>
<td><b>Age</b></td>
</tr>
 <#list centList.slots as slot>
 <tr>
<td>${slot.date}</td>
<td>${slot.seat}</td>
<td>${slot.dose1}</td>
<td>${slot.dose2}</td>
<td>${slot.min_age}+</td>
</tr>
 
 </#list>
</#list>

<br>
<br>

</body>
<p>If you did not subscribe for this, please visit <a href="http://http://35.200.204.248/cowin">Covid Vaccine Alert</a> website and unsubcribe yourself.
<p>Thank you.
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br
<p>N.B.: We are not validating the data. We are using the data from cowin and check it to send email notification. 
</html>