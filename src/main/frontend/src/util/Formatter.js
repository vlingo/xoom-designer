export default {
    buildSettingsFullname(context) {
        if(context && context.packageName) {
            return context.packageName.replace(/\\./g, "-");
        }
        return "settings";
    }
}
