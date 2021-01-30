<script>
	import { TextField } from "svelte-materialify/src";
	import { requireRule } from "../../validators";

	export let path = "";
	export let requireEntityLoad = false;

	$: changed(requireEntityLoad);
	function changed(requireEntityLoad) {
		console.log(requireEntityLoad);
		if(requireEntityLoad) {
			path = regexPrefix(/^\/\{id\}\//, "/{id}/", path, "/{id}/"+path);
		} else {
			path = hasPrefix(/^\/\{id\}\//, path) ? path.substring(6) : path;
		}
	}

	function pathShouldHavePrefix(e) {
		let value = e.target.value;
		path = regexPrefix(/^\/\{id\}\//, "/{id}/", value, path); //doesn't react, therefor we need to set value manually:
		e.target.value = path;
		console.log(e.target.value, path);
	}
	function regexPrefix (regex, prefix, newValue, oldValue) {
		return regex.test(newValue) ? newValue : (newValue ? oldValue : prefix);
	}
	function hasPrefix(regex, value) {
		return regex.test(value);
	}
	
	const handleInput = (e) => {
		if(requireEntityLoad) {
			pathShouldHavePrefix(e);
		} else {
			path = e.target.value;
		}
	}

	$: console.log(path);
	
</script>

<TextField value={path} rules={[requireRule]} on:input={handleInput} validateOnBlur={!path}>Path</TextField>