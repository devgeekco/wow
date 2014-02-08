WOW - Who Owes Who?
===

Designed and implemented algorithm for calculating 'Who Owes Who' in Group expenses.

Algorithm
_________

1. Save users in HashMap - Key: username, Value: total expense
2. Caculate average of expense 'avge'
3. Substracts avgage expense with each users total expense and save uses in different HashMap based of positive and negative return i.e. negativeUsers<>, positiveUsers<>
4. Sort negativeUsers<> in increasing order and positiveUsers<> in decreasing order.
5. Subtract positiveUser.value against negativeUsers.value in a Recursive function
	
generateReport(positiveUser, negativeuser) {
	
	if(positiveUser.size == 0 or negativeUser.size == 0)
		return true;

	subValue = positiveUser.Value - negativeUser.value;
	if(subValue == 0 ) then
		print "negativeUser.key owes positiveUser.key Amount: positiveUser.value";
		positiveUser.removeKey; negativeUser.removeUser;
	else if (subValue > 0 ) then
		print "negativeUser.key owes positiveUser.key Amount: negativeUser.value.abs";
		positiveUser.put(key, subValue); negativeUser.removeUser
	else if (subValue < 0) then
		print "negativeUser.key owes positiveUser.key Amount: positiveUser.value ";
		positiveUser.removeUser; negativeUser.put(key, subValue);
	else
		print "Error!"

	// Recursive call
	generateReport(positiveuser, negativeUser);

	return true;
	}    
