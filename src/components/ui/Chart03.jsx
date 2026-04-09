import ApexCharts from "apexcharts";
import React, { useEffect, useState } from 'react';
export default function Chart03() {

    const chartThreeOptions = {
        series: [
            {
                name: "Sales",
                data: [180, 190, 170, 160, 175, 165, 170, 205, 230, 210, 240, 235],
            },
            {
                name: "Revenue",
                data: [40, 30, 50, 40, 55, 40, 70, 100, 110, 120, 150, 140],
            },
        ],
        legend: {
            show: false,
            position: "top",
            horizontalAlign: "left",
        },
        colors: ["#465FFF", "#9CB9FF"],
        chart: {
            fontFamily: "Outfit, sans-serif",
            height: 310,
            type: "area",
            toolbar: {
                show: false,
            },
        },
        fill: {
            gradient: {
                enabled: true,
                opacityFrom: 0.55,
                opacityTo: 0,
            },
        },
        stroke: {
            curve: "straight",
            width: ["2", "2"],
        },

        markers: {
            size: 0,
        },
        labels: {
            show: false,
            position: "top",
        },
        grid: {
            xaxis: {
                lines: {
                    show: false,
                },
            },
            yaxis: {
                lines: {
                    show: true,
                },
            },
        },
        dataLabels: {
            enabled: false,
        },
        tooltip: {
            x: {
                format: "dd MMM yyyy",
            },
        },
        xaxis: {
            type: "category",
            categories: [
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
            ],
            axisBorder: {
                show: false,
            },
            axisTicks: {
                show: false,
            },
            tooltip: false,
        },
        yaxis: {
            title: {
                style: {
                    fontSize: "0px",
                },
            },
        },
    };

    const chartSelector = document.querySelectorAll("#chartThree");

    if (chartSelector.length) {
        const chartThree = new ApexCharts(
            document.querySelector("#chartThree"),
            chartThreeOptions,
        );
        chartThree.render();
    }
    const [selected, setSelected] = useState("");
    const getButtonClass = (tabName) => {
        const baseClass = "text-theme-sm rounded-md px-3 py-2 font-medium hover:text-gray-900 dark:hover:text-white transition-colors";
        const activeClass = "shadow-theme-xs text-gray-900 dark:text-white bg-white dark:bg-gray-800";
        const inactiveClass = "text-gray-500 dark:text-gray-400";
        return `${baseClass} ${selected === tabName ? activeClass : inactiveClass}`;
    };

    useEffect(() => {
        setSelected("overview");
    }, []);

    return (
        <div className="rounded-2xl border border-gray-200 bg-white px-5 pt-5 pb-5 sm:px-6 sm:pt-6 dark:border-gray-300 dark:bg-white/3">
            <div className="mb-6 flex flex-col gap-5 sm:flex-row sm:justify-between">
                <div className="w-full">
                    <h3 className="text-lg font-semibold text-gray-800 dark:text-gray-400">
                        Statistics
                    </h3>
                    <p className="text-theme-sm mt-1 text-gray-500 dark:text-gray-400">
                        Target you've set for each month
                    </p>
                </div>

                <div className="flex w-full items-start gap-3 sm:justify-end">
                    <div className="inline-flex w-fit items-center gap-0.5 rounded-lg bg-gray-100 p-0.5">
                        <button
                            onClick={() => setSelected('overview')}
                            className={getButtonClass('overview')} >
                            Overview
                        </button>
                        <button
                            onClick={() => setSelected('sales')}
                            className={getButtonClass('sales')}
                        >
                            Sales
                        </button>
                        <button
                            onClick={() => setSelected('revenue')}
                            className={getButtonClass('revenue')}
                        >
                            Revenue
                        </button>
                    </div>

                    <div className="relative max-w-41">
                        <input
                            className="datepicker text-theme-sm shadow-theme-xs h-10 w-full max-w-11 rounded-lg border border-gray-200 bg-white py-2.5 pr-4 pl-8.5 font-medium text-gray-700 focus:ring-0 focus:outline-hidden focus-visible:outline-hidden xl:max-w-fit xl:pl-11 dark:text-gray-400"
                            placeholder="Selected date"
                            readOnly
                        />
                        <div className="pointer-events-none absolute inset-0 right-auto left-3 flex items-center">
                            <svg
                                className="fill-gray-700 dark:fill-gray-400"
                                width="20"
                                height="20"
                                viewBox="0 0 20 20"
                                fill="none"
                                xmlns="http://www.w3.org/2000/svg"
                            >
                                <path
                                    fillRule="evenodd"
                                    clipRule="evenodd"
                                    d="M6.66683 1.54199C7.08104 1.54199 7.41683 1.87778 7.41683 2.29199V3.00033H12.5835V2.29199C12.5835 1.87778 12.9193 1.54199 13.3335 1.54199C13.7477 1.54199 14.0835 1.87778 14.0835 2.29199V3.00033L15.4168 3.00033C16.5214 3.00033 17.4168 3.89576 17.4168 5.00033V7.50033V15.8337C17.4168 16.9382 16.5214 17.8337 15.4168 17.8337H4.5835C3.47893 17.8337 2.5835 16.9382 2.5835 15.8337V7.50033V5.00033C2.5835 3.89576 3.47893 3.00033 4.5835 3.00033L5.91683 3.00033V2.29199C5.91683 1.87778 6.25262 1.54199 6.66683 1.54199ZM6.66683 4.50033H4.5835C4.30735 4.50033 4.0835 4.72418 4.0835 5.00033V6.75033H15.9168V5.00033C15.9168 4.72418 15.693 4.50033 15.4168 4.50033H13.3335H6.66683ZM15.9168 8.25033H4.0835V15.8337C4.0835 16.1098 4.30735 16.3337 4.5835 16.3337H15.4168C15.693 16.3337 15.9168 16.1098 15.9168 15.8337V8.25033Z"
                                />
                            </svg>
                        </div>
                    </div>
                </div>
            </div>

            <div className="custom-scrollbar max-w-full overflow-x-auto">
                <div id="chartThree" className="-ml-4 min-w-175 pl-2"></div>
            </div>
        </div>
    );
};
