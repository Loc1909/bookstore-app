import React, { useState } from "react";
import Chart from "react-apexcharts";

const Chart02 = () => {
    const [isOpen, setIsOpen] = useState(false);

    // Cấu hình ApexCharts
    const chartOptions = {
        colors: ["#465FFF"],
        chart: {
            fontFamily: "Outfit, sans-serif",
            type: "radialBar",
            sparkline: { enabled: true },
        },
        plotOptions: {
            radialBar: {
                startAngle: -100,
                endAngle: 100,
                hollow: {
                    size: "75%",
                },
                track: {
                    background: "rgba(228, 231, 236, 0.1)",
                    strokeWidth: "100%",
                    margin: 5,
                },
                dataLabels: {
                    name: {
                        show: false,
                    },
                    value: {
                        fontSize: "36px",
                        fontWeight: "700",
                        offsetY: 40,
                        color: "#FF00FF",
                        formatter: (val) => val + "%",
                    },
                },
            },
        },
        fill: {
            type: "solid",
            colors: ["#465FFF"],
        },
        stroke: { lineCap: "round" },
        labels: ["Progress"],
    };

    const chartSeries = [75.55];

    return (
        <div className="rounded-2xl border border-gray-200 bg-gray-100 dark:border-gray-300 dark:bg-white/3">
            <div className="shadow-default rounded-2xl bg-white px-5 pb-11 pt-5 dark:bg-gray-900 sm:px-6 sm:pt-6">
                <div className="flex justify-between">
                    <div>
                        <h3 className="text-lg font-semibold text-gray-800 dark:text-red-600">
                            Monthly Target
                        </h3>
                        <p className="mt-1 text-theme-sm text-gray-500 dark:text-red-400">
                            Target you've set for each month
                        </p>
                    </div>

                    {/* Dropdown Menu */}
                    <div className="relative h-fit">
                        <button
                            onClick={() => setIsOpen(!isOpen)}
                            className={`${isOpen ? "text-gray-700 dark:text-white" : "text-gray-400 hover:text-gray-700 dark:hover:text-white"
                                }`}
                        >
                            <svg className="fill-current" width="24" height="24" viewBox="0 0 24 24">
                                <path
                                    fillRule="evenodd"
                                    clipRule="evenodd"
                                    d="M10.2441 6C10.2441 5.0335 11.0276 4.25 11.9941 4.25H12.0041C12.9706 4.25 13.7541 5.0335 13.7541 6C13.7541 6.9665 12.9706 7.75 12.0041 7.75H11.9941C11.0276 7.75 10.2441 6.9665 10.2441 6ZM10.2441 18C10.2441 17.0335 11.0276 16.25 11.9941 16.25H12.0041C12.9706 16.25 13.7541 17.0335 13.7541 18C13.7541 18.9665 12.9706 19.75 12.0041 19.75H11.9941C11.0276 19.75 10.2441 18.9665 10.2441 18ZM11.9941 10.25C11.0276 10.25 10.2441 11.0335 10.2441 12C10.2441 12.9665 11.0276 13.75 11.9941 13.75H12.0041C12.9706 13.75 13.7541 12.9665 13.7541 12C13.7541 11.0335 12.9706 10.25 12.0041 10.25H11.9941Z"
                                />
                            </svg>
                        </button>

                        {isOpen && (
                            <div className="absolute right-0 top-full z-40 w-40 space-y-1 rounded-2xl border border-gray-200 bg-white p-2 shadow-theme-lg dark:border-gray-300 dark:bg-gray-dark">
                                <button className="flex w-full rounded-lg px-3 py-2 text-left text-theme-xs font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-white/5 dark:hover:text-gray-300">
                                    View More
                                </button>
                                <button className="flex w-full rounded-lg px-3 py-2 text-left text-theme-xs font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-white/5 dark:hover:text-gray-300">
                                    Delete
                                </button>
                            </div>
                        )}
                    </div>
                </div>

                {/* Chart Section */}
                <div className="relative h-55 flex justify-center">
                    <Chart options={chartOptions} series={chartSeries} type="radialBar" height={350} width="100%" />
                </div>

                <p className="mx-auto mt-1.5 w-full max-w-95 text-center text-sm text-gray-500 sm:text-base">
                    You earn $3287 today, it's higher than last month. Keep up your good work!
                </p>
            </div>

            {/* Footer Stats */}
            <div className="flex items-center justify-center gap-5 px-6 py-3.5 sm:gap-8 sm:py-5">
                <StatItem label="Target" color="#EF4444" value="$20K" isUp={false} />
                <div className="h-7 w-px bg-gray-200 dark:bg-gray-800"></div>
                <StatItem label="Revenue" color="#22C55E" value="$20K" isUp={true} />
                <div className="h-7 w-px bg-gray-200 dark:bg-gray-800"></div>
                <StatItem label="Today" color="#22C55E" value="$20K" isUp={true} />
            </div>
        </div>
    );
};

const StatItem = ({ label, value, color, isUp }) => (
    <div className="text-center">
        <p className="text-gray-400 text-sm mb-1">{label}</p>
        <div className="flex flex-col items-center gap-1">
            <span className="text-gray-800 font-bold text-lg">{value}</span>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" className={isUp ? "" : "rotate-180"}>
                <path d="M12 4v16m0-16l-7 7m7-7l7 7" stroke={color} strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
        </div>
    </div>
);

export default Chart02;