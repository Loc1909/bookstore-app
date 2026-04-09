import React, { useState } from "react";
import { InputText } from "../components/ui/InputText";
import { SelectInput } from "../components/ui/SelectedInput";
import { Textarea } from "../components/ui/TextArea";

export default function FormPage() {
    const [formData, setFormData] = useState({
        text: "",
        selected: "",
        textarea: ""
    });
    const handleUpdate = (field, value) => {
        setFormData((prev) => ({
            ...prev,
            [field]: value
        }));
    };
    const selectOptions = [
        { label: "Marketing", value: "marketing" },
        { label: "Content", value: "content" },
        { label: "Design", value: "design" },
        { label: "Research", value: "research" },
        { label: "Development", value: "development" },
        { label: "Template", value: "template" },
    ];
    const handleSubmit = (e) => {
        e.preventDefault();
        alert(`
      - Text Input: ${formData.text}
      - Selected: ${formData.selected}
      - Textarea: ${formData.textarea}
    `);
    }

    return (
        <main className="p-4 md:p-6 shadow-theme-xs rounded-xl border border-gray-200 bg-white dark:border-gray-800 dark:bg-white/3">
            <div className="mb-6">
                <h3 className="text-lg font-semibold text-gray-800 ">
                    Form Elements
                </h3>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
                <InputText
                    label="Input with Placeholder"
                    placeholder="Text input here"
                    value={formData.text}
                    onChange={(val) => handleUpdate("text", val)}
                />

                <SelectInput
                    label="Select Input"
                    placeholder="Select Option"
                    options={selectOptions}
                    value={formData.selected}
                    onChange={(val) => handleUpdate("selected", val)}
                />

                <Textarea
                    label="Description"
                    placeholder="Enter a description..."
                    value={formData.textarea}
                    onChange={(e) => handleUpdate("textarea", e.target.value)}
                />

                {/* Submit Button */}
                <div className="flex justify-end gap-4.5">
                    <button
                        className="flex justify-center rounded-lg border border-gray-300 px-6 py-2.5 font-medium text-gray-700 hover:shadow-1 dark:border-gray-700 dark:text-gray-500"
                        type="button" >
                        Cancel
                    </button>
                    <button
                        className="flex justify-center rounded-lg bg-blue-600 px-6 py-2.5 font-medium text-white hover:bg-opacity-90"
                        type="submit" >
                        Save
                    </button>
                </div>
            </form>
        </main>
    );
}