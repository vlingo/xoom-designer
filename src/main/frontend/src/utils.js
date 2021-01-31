export function formatArrayForSelect(array) {
	return array.reduce((acc, cur) => {
		if (cur && acc.findIndex(a => a.name === cur) < 0) {
			acc.push({
				name: cur,
				value: cur
			});
		}
		return acc;
	}, []);
}