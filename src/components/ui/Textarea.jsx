export const Textarea = ({ label, placeholder, rows = 6, ...props }) => (
    <div>
        <label className="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
            {label}
        </label>
        <textarea
            rows={rows}
            placeholder={placeholder}
            className="w-full rounded-lg border border-gray-300 bg-gray-50 px-4 py-2.5 text-sm text-gray-800 outline-none transition focus:border-blue-500  dark:border-gray-700 dark:bg-gray-900 dark:text-gray-400 dark:placeholder:text-white/30 dark:focus:border-blue-500"
            {...props}
        ></textarea>
    </div>
);