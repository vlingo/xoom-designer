
const root = "api"

export async function get(path) {
	const res = await fetch(root + path);
	return res;
}

export async function post(path, body) {
	const res = await fetch(root + path, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json; charset=utf-8'},
		body: JSON.stringify(body)
	});
	return res;
}

export async function put(path, body) {
	const res = await fetch(root + path, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json; charset=utf-8'},
		body: JSON.stringify(body)
	});
	return res;
}

export async function patch(path, body) {
	const res = await fetch(root + path, {
		method: 'PATCH',
		headers: { 'Content-Type': 'application/json; charset=utf-8'},
		body: body,
	});
	return res;
}

let client = { get, post, put, patch }

export default client;