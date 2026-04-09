import { useState } from "react";

export const SelectInput = ({ label, options, value, onChange, placeholder }) => {
    const [isOptionSelected, setIsOptionSelected] = useState(false);

    return (
        <div>
            <label className="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
                {label}
            </label>
            <div className="relative z-20">
                <select
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    className={`h-11 w-full appearance-none rounded-lg border border-gray-300 bg-gray-50 px-4 py-2.5 pr-11 text-sm outline-none transition focus:border-blue-500  dark:border-gray-700 dark:bg-gray-900 ${isOptionSelected ? "text-gray-800 dark:text-gray-400" : "text-gray-400"
                        }`}
                >
                    <option value="" disabled className="text-gray-400">
                        {placeholder}
                    </option>
                    {options.map((opt) => (
                        <option key={opt.value} value={opt.value} className="text-gray-700 dark:text-gray-400">
                            {opt.label}
                        </option>
                    ))}
                </select>
                <span className="pointer-events-none absolute right-4 top-1/2 z-30 -translate-y-1/2 text-gray-500">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                        <path d="M4.79175 7.396L10.0001 12.6043L15.2084 7.396" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                    </svg>
                </span>
            </div>
        </div>
    );
};