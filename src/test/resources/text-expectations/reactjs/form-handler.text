import {useCallback, useState} from "react";

const useFormHandler = (defaultForm) => {

  const [form, setForm] = useState(defaultForm);

  const setObjectValue = useCallback((obj, name, value) => {
    const i = name.indexOf('.');
    if (i > -1) {
      const path1 = name.substring(0, i);
      const path2 = name.substring(i + 1);
      if (!obj[path1]) {
        obj[path1] = {};
      }
      if (Array.isArray(obj))
        setObjectValue(obj[0][path1], path2, value);
      else
        setObjectValue(obj[path1], path2, value);
    } else {
      if (Array.isArray(obj) && obj[0] !== undefined)
        obj[0][name] = value;
      else
        obj[name] = value;
    }
  }, []);

  const onFormValueChange = useCallback((e) => {
    const name = e.target.name;
    const value = e.target.value;
    setForm((form) => {
      const result = {...form};
      setObjectValue(result, name, value);
      return result;
    });
  }, [setObjectValue]);

  const resetForm = useCallback(() => {
    console.log('resetting form with', defaultForm);
    setForm(defaultForm);
  }, [defaultForm]);

  return [
    form, onFormValueChange, resetForm
  ];
}

export default useFormHandler;
