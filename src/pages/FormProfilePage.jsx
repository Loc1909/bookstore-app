import React, { useState } from "react";
import { InputText } from "../components/ui/InputText";
import { SelectInput } from "../components/ui/SelectedInput";
import { DatePicker } from "../components/ui/DatePicker";

export default function FormProfilePage() {
    const selectOptions = [
        { label: "Male", value: "male" },
        { label: "Female", value: "female" },
        { label: "Other", value: "other" },
    ];
    const [profile, setProfile] = useState({
        fullName: "Dung Truong",
        email: "dz@gmail.com",
        birthday: "2007-05-15",
        phone: "0123456879",
        gender: "male",
    });
    const handleUpdate = (field, value) => {
        setProfile((prev) => ({ ...prev, [field]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Form submit for ${profile.fullName}`);

    };

    return (
        <div className="mx-auto max-w-4xl">
            <div className="rounded-xl border border-gray-200 bg-white shadow-sm dark:border-gray-800 dark:bg-white/3">
                {/* Header */}
                <div className="border-b border-gray-200 px-6 py-4 dark:border-gray-800">
                    <h3 className="font-semibold text-gray-800 dark:text-gray-500">
                        Edit Profile
                    </h3>
                </div>
                <form onSubmit={handleSubmit} className="p-6.5 m-3">
                    {/* Avatar */}
                    <div className="mb-4 flex items-center gap-4">
                        <div className="h-20 w-20 rounded-full bg-gray-200 dark:bg-gray-800 flex items-center justify-center overflow-hidden">
                            <span className="text-gray-400">D</span>
                        </div>
                        <div>
                            <button type="button" className="text-sm font-medium text-blue-600 hover:underline">
                                Change Photo
                            </button>
                        </div>
                    </div>
                    <div className="grid grid-cols-1 gap-6">
                        <InputText
                            label="Full Name"
                            placeholder="Enter your name"
                            value={profile.fullName}
                            onChange={(val) => handleUpdate("fullName", val)}
                        />

                        <InputText
                            label="Email Address"
                            type="email"
                            placeholder="abczxc@gmail.com"
                            value={profile.email}
                            onChange={(val) => handleUpdate("email", val)}
                        />

                        <InputText
                            label="Phone Number"
                            placeholder="+84 123 456 789"
                            value={profile.phone}
                            onChange={(val) => handleUpdate("phone", val)}
                        />
                        <DatePicker
                            label="Date of Birth"
                            value={profile.birthday}
                            onChange={(val) => handleUpdate("birthday", val)}
                        />
                        <SelectInput
                            label="Gender"
                            placeholder="Select Gender"
                            options={selectOptions}
                            value={profile.gender}
                            onChange={(val) => handleUpdate("gender", val)}
                        />
                    </div>

                    {/* Nút hành động */}
                    <div className="mt-8 flex justify-end gap-4">
                        <button
                            type="button"
                            className="rounded-lg border border-gray-300 px-8 py-2.5 font-medium text-gray-700 transition hover:bg-gray-50 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-800"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="rounded-lg bg-blue-600 px-8 py-2.5 font-medium text-white transition hover:bg-blue-700"
                        >
                            Save Changes
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}