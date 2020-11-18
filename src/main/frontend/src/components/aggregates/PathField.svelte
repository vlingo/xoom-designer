<script>
	import TextField from "svelte-materialify/src/components/TextField";
	import { requireRule } from "../../validators";

	export let path = "";
	export let requireEntityLoad = false;

	$: changed(requireEntityLoad);
	function changed(requireEntityLoad) {
		if(requireEntityLoad) {
			path = regexPrefix(/^\/\{id\}\//, "/{id}/", path, "/{id}/"+path);
		} else {
			path = path.substring(6);
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
	
	const handleInput = (e) => {
		if(requireEntityLoad) {
			pathShouldHavePrefix(e);
		} else {
			path = e.target.value;
		}
	}

	$: console.log(path);
	
</script>

<TextField class="ma-2" value={path} rules={[requireRule]} on:input={handleInput} validateOnBlur={!path}>Path</TextField>