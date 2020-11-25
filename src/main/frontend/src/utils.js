export function formatArrayForSelect(array) {
	return array.map(
		element => ({ name: element, value: element })
	)
}